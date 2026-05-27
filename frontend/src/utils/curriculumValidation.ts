export const getTodayInputDate = () => new Date().toISOString().slice(0, 10);

export const hasText = (value?: string | null) => Boolean(value?.trim());

export const isValidInputDate = (value?: string | null) => {
  if (!hasText(value)) return false;
  const date = new Date(`${value}T00:00:00.000Z`);
  return !Number.isNaN(date.getTime());
};

export const isFutureInputDate = (value?: string | null) => {
  if (!isValidInputDate(value)) return false;
  return String(value).slice(0, 10) > getTodayInputDate();
};

export const isAfterInputDate = (start?: string | null, end?: string | null) => {
  if (!isValidInputDate(start) || !isValidInputDate(end)) return false;
  return String(start).slice(0, 10) > String(end).slice(0, 10);
};

export const addRequiredTextError = (errors: string[], value: string | undefined | null, label: string) => {
  if (!hasText(value)) errors.push(`${label} es obligatorio.`);
};

export const addRequiredDateError = (errors: string[], value: string | undefined | null, label: string) => {
  if (!isValidInputDate(value)) errors.push(`${label} es obligatorio.`);
};

export const addDateNotFutureError = (errors: string[], value: string | undefined | null, label: string) => {
  if (hasText(value) && isFutureInputDate(value)) errors.push(`${label} no puede ser una fecha futura.`);
};

export const addDateOrderError = (
  errors: string[],
  start: string | undefined | null,
  end: string | undefined | null,
  message: string,
) => {
  if (hasText(start) && hasText(end) && isAfterInputDate(start, end)) errors.push(message);
};

export const addNumberRangeError = (
  errors: string[],
  value: number | undefined | null,
  label: string,
  min: number,
  max?: number,
) => {
  if (value === undefined || value === null || Number.isNaN(Number(value))) {
    errors.push(`${label} es obligatorio.`);
    return;
  }

  const numericValue = Number(value);

  if (numericValue < min) {
    errors.push(`${label} debe ser mayor o igual a ${min}.`);
  }

  if (max !== undefined && numericValue > max) {
    errors.push(`${label} debe ser menor o igual a ${max}.`);
  }
};

export const joinValidationErrors = (errors: string[]) => [...new Set(errors)].join(" ");
