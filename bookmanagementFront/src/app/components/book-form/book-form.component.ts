import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BookService, CreateBookRequest, UpdateBookRequest, UpdateProgressRequest } from '../../services/book.service';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category';
import { Book } from '../../models/book';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-book-form',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: './book-form.component.html',
  styleUrl: './book-form.component.scss'
})
export class BookFormComponent implements OnInit {
  bookForm: FormGroup;
  progressForm: FormGroup;
  categories: Category[] = [];
  book: Book | null = null;
  isEditMode = false;
  showProgressSection = false;
  loading = false;
  error = '';
  success = '';

  constructor(
    private fb: FormBuilder,
    private bookService: BookService,
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.bookForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(1)]],
      author: ['', [Validators.required, Validators.minLength(1)]],
      totalPages: ['', [Validators.required, Validators.min(1)]],
      categoryId: [''],
      notes: ['']
    });

    this.progressForm = this.fb.group({
      currentPage: ['', [Validators.required, Validators.min(0)]],
      isFinished: [false],
      notes: ['']
    });
  }

  ngOnInit(): void {
    this.loadCategories();
    
    const bookId = this.route.snapshot.paramMap.get('id');
    if (bookId && bookId !== 'new') {
      this.isEditMode = true;
      this.showProgressSection = true;
      this.loadBook(+bookId);
    }
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des catégories:', error);
      }
    });
  }

  loadBook(id: number): void {
    this.loading = true;
    this.bookService.getBook(id).subscribe({
      next: (book) => {
        this.book = book;
        this.bookForm.patchValue({
          title: book.title,
          author: book.author,
          totalPages: book.totalPages,
          categoryId: book.category?.id || '',
          notes: book.notes || ''
        });
        this.progressForm.patchValue({
          currentPage: book.currentPage,
          isFinished: book.isFinished,
          notes: book.notes || ''
        });
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors du chargement du livre';
        this.loading = false;
        console.error(error);
      }
    });
  }

  onSubmitBook(): void {
    if (this.bookForm.valid) {
      this.loading = true;
      this.error = '';
      
      const formValue = this.bookForm.value;
      const request: CreateBookRequest | UpdateBookRequest = {
        title: formValue.title.trim(),
        author: formValue.author.trim(),
        totalPages: +formValue.totalPages,
        // categoryId: formValue.categoryId || null,
        categoryId: formValue.categoryId ? Number(formValue.categoryId) : undefined,
        notes: formValue.notes?.trim() || null
      };

      const operation = this.isEditMode && this.book
        ? this.bookService.updateBook(this.book.id, request as UpdateBookRequest)
        : this.bookService.createBook(request as CreateBookRequest);

      operation.subscribe({
        next: (book) => {
          this.success = this.isEditMode ? 'Livre modifié avec succès' : 'Livre ajouté avec succès';
          this.loading = false;
          
          if (!this.isEditMode) {
            // Nouveau livre créé, rediriger vers l'édition
            setTimeout(() => {
              this.router.navigate(['/book', book.id]);
            }, 1500);
          } else {
            // Livre modifié, recharger les données
            this.book = book;
            setTimeout(() => this.success = '', 3000);
          }
        },
        error: (error) => {
          this.error = error.error?.error || 'Erreur lors de la sauvegarde';
          this.loading = false;
          console.error(error);
        }
      });
    } else {
      this.markFormGroupTouched(this.bookForm);
    }
  }

  onSubmitProgress(): void {
    if (this.progressForm.valid && this.book) {
      this.loading = true;
      this.error = '';
      
      const formValue = this.progressForm.value;
      const request: UpdateProgressRequest = {
        currentPage: +formValue.currentPage,
        isFinished: formValue.isFinished
      };

      // Validation : currentPage ne peut pas dépasser totalPages
      if (request.currentPage > this.book.totalPages) {
        this.error = 'Le nombre de pages lues ne peut pas dépasser le total';
        this.loading = false;
        return;
      }

      this.bookService.updateProgress(this.book.id, request).subscribe({
        next: (book) => {
          this.book = book;
          this.success = 'Progression mise à jour avec succès';
          this.loading = false;
          setTimeout(() => this.success = '', 3000);
        },
        error: (error) => {
          this.error = error.error?.error || 'Erreur lors de la mise à jour';
          this.loading = false;
          console.error(error);
        }
      });
    } else {
      this.markFormGroupTouched(this.progressForm);
    }
  }

  markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    const field = formGroup.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getFieldError(formGroup: FormGroup, fieldName: string): string {
    const field = formGroup.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) return `${fieldName} est obligatoire`;
      if (field.errors['minlength']) return `${fieldName} est trop court`;
      if (field.errors['min']) return `${fieldName} doit être positif`;
    }
    return '';
  }

  goBack(): void {
    this.router.navigate(['']);
  }

  deleteBook(): void {
    if (this.book && confirm(`Êtes-vous sûr de vouloir supprimer "${this.book.title}" ?`)) {
      this.bookService.deleteBook(this.book.id).subscribe({
        next: () => {
          this.router.navigate(['']);
        },
        error: (error) => {
          this.error = 'Erreur lors de la suppression';
          console.error(error);
        }
      });
    }
  }
}
