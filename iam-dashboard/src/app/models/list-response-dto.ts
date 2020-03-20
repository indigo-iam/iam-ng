export interface ListResponseDTO<T> {
  kind: string;
  totalResults: number;
  itemsPerPage: number;
  startIndex: number;
  resources: T[];
}
