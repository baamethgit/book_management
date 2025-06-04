import { Category } from "./category";

export interface Book {
  id: number;
  title: string;
  author: string;
  totalPages: number;
  coverImage?: string;
  currentPage: number;
  isFinished: boolean;
  notes?: string;
  progressPercentage: number;
  remainingPages: number;
  createdAt: string;
  category?: Category;
}
