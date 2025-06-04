import { Component, OnInit } from '@angular/core';
import { Book } from '../../models/book';
import { Category } from '../../models/category';
import { Stats } from '../../models/stats';
import { BookService } from '../../services/book.service';
import { CategoryService } from '../../services/category.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StatsService } from '../../services/stats.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  books: Book[] = [];
  categories: Category[] = [];
  stats: Stats | null = null;
  selectedCategory: number | null = null;
  loading = true;
  error = '';

  constructor(
    private bookService: BookService,
    private categoryService: CategoryService,
    private statsService: StatsService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadData();
  }


loadData(): void {
  this.loading = true;
  
  forkJoin({
    books: this.bookService.getBooks(),
    categories: this.categoryService.getCategories(),
    stats: this.statsService.getStats()
  }).subscribe({
    next: ({ books, categories, stats }) => {
      this.books = books || [];
      this.categories = categories || [];
      this.stats = stats || null;
      this.loading = false;
    },
    error: (error) => {
      this.error = 'Erreur lors du chargement des données';
      this.loading = false;
      console.error(error);
    }
  });
}

  filterByCategory(categoryId: number | null): void {
    this.selectedCategory = categoryId;
    this.loading = true;
    this.bookService.getBooks(categoryId || undefined).subscribe({
      next: (books) => {
        this.books = books;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors du filtrage';
        this.loading = false;
        console.error(error);
      }
    });
  }

  deleteBook(book: Book): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer "${book.title}" ?`)) {
      this.bookService.deleteBook(book.id).subscribe({
        next: () => {
          this.books = this.books.filter(b => b.id !== book.id);
        },
        error: (error) => {
          this.error = 'Erreur lors de la suppression';
          console.error(error);
        }
      });
    }
  }

  navigateToBook(bookId: number): void {
    this.router.navigate(['/books', bookId]);
  }

  navigateToAddBook(): void {
    console.log("clicked")
    this.router.navigate(['/books/new']);
  }

  navigateToCategories(): void {
    this.router.navigate(['/categories']);
  }
}