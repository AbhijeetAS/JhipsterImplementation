export interface ICourses {
  id?: string;
  courseName?: string | null;
  courseCost?: number | null;
}

export const defaultValue: Readonly<ICourses> = {};
