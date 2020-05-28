import { Observable } from 'rxjs';

export abstract class PaginatedService {
    abstract getPaginated(realm: string, startIndex: number, count: number): Observable<any>;
}