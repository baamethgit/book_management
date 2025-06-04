import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CategoryService, Category } from '../../services/category.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.scss'
})
export class CategoriesComponent implements OnInit {
  categories: Category[] = [];
  loading = false;
  error = '';
  
  // Formulaire
  showForm = false;
  editingId: number | null = null;
  name = '';
  description = '';

  constructor(
    private categoryService: CategoryService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.loading = true;
    this.categoryService.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors du chargement des catégories';
        this.loading = false;
        console.error(error);
      }
    });
  }

  showAddForm(): void {
    this.showForm = true;
    this.editingId = null;
    this.name = '';
    this.description = '';
  }

  editCategory(category: Category): void {
    this.showForm = true;
    this.editingId = category.id;
    this.name = category.name;
    this.description = category.description;
  }

  cancelForm(): void {
    this.showForm = false;
    this.editingId = null;
    this.name = '';
    this.description = '';
  }

  saveCategory(): void {
    if (!this.name.trim()) {
      this.error = 'Le nom est obligatoire';
      return;
    }

    const operation = this.editingId 
      ? this.categoryService.updateCategory(this.editingId, this.name, this.description)
      : this.categoryService.createCategory(this.name, this.description);

    operation.subscribe({
      next: () => {
        this.loadCategories();
        this.cancelForm();
        this.error = '';
      },
      error: (error) => {
        if (error.error?.error) {
          this.error = error.error.error;
        } else {
          this.error = 'Erreur lors de la sauvegarde';
        }
        console.error(error);
      }
    });
  }

  deleteCategory(category: Category): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer "${category.name}" ?`)) {
      this.categoryService.deleteCategory(category.id).subscribe({
        next: () => {
          this.loadCategories();
        },
        error: (error) => {
          if (error.error?.error) {
            this.error = error.error.error;
          } else {
            this.error = 'Erreur lors de la suppression';
          }
          console.error(error);
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}