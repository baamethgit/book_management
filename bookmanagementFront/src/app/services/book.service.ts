import { Injectable } from '@angular/core';
import { Book } from '../models/book';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment.development';


export interface CreateBookRequest {
  title: string;
  author: string;
  totalPages: number;
  categoryId?: number;
  notes?: string;
}

export interface UpdateBookRequest {
  title: string;
  author: string;
  totalPages: number;
  categoryId?: number;
  notes?: string;
}

export interface UpdateProgressRequest {
  currentPage: number;
  isFinished?: boolean;
  notes?: string;
}

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiUrl = `${environment.apiUrl}/api/books`;

  constructor(private http: HttpClient) { }

  getBooks(categoryId?: number): Observable<Book[]> {
    let params = new HttpParams();
    if (categoryId) {
      params = params.set('categoryId', categoryId.toString());
    }
    return this.http.get<Book[]>(this.apiUrl, { params });
  }

  getBook(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/${id}`);
  }

  createBook(request: CreateBookRequest): Observable<Book> {
    return this.http.post<Book>(this.apiUrl, request);
  }

  updateBook(id: number, request: UpdateBookRequest): Observable<Book> {
    return this.http.put<Book>(`${this.apiUrl}/${id}`, request);
  }

  updateProgress(id: number, request: UpdateProgressRequest): Observable<Book> {
    return this.http.patch<Book>(`${this.apiUrl}/${id}/progress`, request);
  }

  deleteBook(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
