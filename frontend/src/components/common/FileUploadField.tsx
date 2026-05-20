import React, { useRef, useState } from "react";
import { fileService, getApiError } from "../../services/api";

interface FileUploadFieldProps {
  label: string;
  value?: string;
  onChange: (url: string) => void;
  required?: boolean;
  disabled?: boolean;
  helperText?: string;
}

const MAX_SIZE_MB = 10;
const ACCEPTED_TYPES = ".pdf,.jpg,.jpeg,.png";

const fileNameFromUrl = (url?: string) => {
  if (!url) return "";
  const clean = url.split("?")[0];
  return clean.substring(clean.lastIndexOf("/") + 1) || "Documento cargado";
};

const FileUploadField: React.FC<FileUploadFieldProps> = ({
  label,
  value,
  onChange,
  required,
  disabled,
  helperText = "PDF, JPG o PNG. Máximo 10 MB.",
}) => {
  const inputRef = useRef<HTMLInputElement>(null);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState("");

  const handleFileChange = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    setError("");

    if (file.size > MAX_SIZE_MB * 1024 * 1024) {
      setError(`El documento no puede superar ${MAX_SIZE_MB} MB.`);
      event.target.value = "";
      return;
    }

    try {
      setUploading(true);
      const uploaded = await fileService.subirArchivo(file);
      onChange(uploaded.url);
      event.target.value = "";
    } catch (err) {
      setError(getApiError(err));
    } finally {
      setUploading(false);
    }
  };

  const handleOpen = async () => {
    if (!value) return;
    setError("");
    try {
      await fileService.verArchivo(value);
    } catch (err) {
      setError(getApiError(err));
    }
  };

  return (
    <div className="form-group">
      <label className="form-label">
        {label} {required && <span className="required">*</span>}
      </label>

      <input
        ref={inputRef}
        type="file"
        accept={ACCEPTED_TYPES}
        className="form-input"
        required={required && !value}
        disabled={disabled || uploading}
        onChange={handleFileChange}
      />

      <small className="text-muted" style={{ display: "block", marginTop: 6 }}>
        {uploading ? "Cargando documento..." : helperText}
      </small>

      {value && (
        <div className="flex gap-2 items-center" style={{ marginTop: 10, flexWrap: "wrap" }}>
          <span className="badge badge-green">Documento cargado</span>
          <span className="text-muted" style={{ fontSize: "0.85rem" }}>{fileNameFromUrl(value)}</span>
          <button type="button" className="btn btn-secondary btn-sm" onClick={handleOpen} disabled={uploading}>
            Ver documento
          </button>
          {!disabled && (
            <button type="button" className="btn btn-danger btn-sm" onClick={() => onChange("")} disabled={uploading}>
              Quitar
            </button>
          )}
        </div>
      )}

      {error && <div className="alert alert-danger" style={{ marginTop: 10 }}>{error}</div>}
    </div>
  );
};

export default FileUploadField;
