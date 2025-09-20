import { Component, inject, signal } from '@angular/core';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService, Question } from '../../services/api.service';

@Component({
  selector: 'app-questions',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgFor, NgIf],
  template: `
    <div class="container">
      <div class="card">
        <h2>Nova questão</h2>
        <form [formGroup]="questionForm" (ngSubmit)="saveQuestion()">
          <label>Enunciado</label>
          <textarea formControlName="prompt" rows="3" placeholder="Digite a questão"></textarea>

          <label>Resposta (opcional)</label>
          <textarea formControlName="answer" rows="2" placeholder="Resposta"></textarea>

          <button class="btn" type="submit" [disabled]="questionForm.invalid">Adicionar</button>
        </form>
      </div>

      <div class="card">
        <h2>Questões cadastradas</h2>
        <p *ngIf="questions().length === 0">Cadastre suas questões para acompanhar as tentativas.</p>
        <div *ngFor="let question of questions()" class="question-item">
          <h3>{{ question.prompt }}</h3>
          <p *ngIf="question.answer">Resposta: {{ question.answer }}</p>
          <div class="actions">
            <button class="btn" (click)="registerAttempt(question, true)">Acertou</button>
            <button class="btn" style="background: #ef4444" (click)="registerAttempt(question, false)">Errou</button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .question-item {
        border-bottom: 1px solid #e5e7eb;
        padding: 1rem 0;
      }

      .question-item:last-child {
        border-bottom: none;
      }

      .actions {
        display: flex;
        gap: 0.75rem;
      }

      .actions .btn {
        flex: 1;
      }
    `
  ]
})
export class QuestionsComponent {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(ApiService);

  readonly questions = signal<Question[]>([]);

  readonly questionForm = this.fb.group({
    prompt: ['', Validators.required],
    answer: ['']
  });

  constructor() {
    this.loadQuestions();
  }

  private loadQuestions(): void {
    this.api.getQuestions().subscribe((questions) => this.questions.set(questions));
  }

  saveQuestion(): void {
    if (this.questionForm.invalid) {
      return;
    }
    const { prompt, answer } = this.questionForm.value;
    this.api
      .createQuestion({ prompt: prompt!, answer: answer ?? undefined })
      .subscribe(() => {
        this.questionForm.reset();
        this.loadQuestions();
      });
  }

  registerAttempt(question: Question, correct: boolean): void {
    this.api.createAttempt(question.id, { correct }).subscribe(() => this.loadQuestions());
  }
}
