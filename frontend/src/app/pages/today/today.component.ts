import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { CommonModule, DatePipe, NgFor, NgIf } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService, StatsResponse, StudySession } from '../../services/api.service';

@Component({
  selector: 'app-today',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgFor, NgIf, DatePipe],
  template: `
    <div class="container">
      <div class="card">
        <h2>Resumo do dia</h2>
        <div *ngIf="stats(); else loadingStats" class="stats-grid">
          <div><strong>{{ stats()?.totalStudyMinutes ?? 0 }}</strong><br />minutos estudados</div>
          <div><strong>{{ stats()?.sessionCount ?? 0 }}</strong><br />sessões</div>
          <div><strong>{{ stats()?.attempts ?? 0 }}</strong><br />questões respondidas</div>
          <div><strong>{{ stats()?.goalProgressMinutes ?? 0 }} / {{ stats()?.goalTargetMinutes ?? 0 }}</strong><br />meta diária</div>
        </div>
        <ng-template #loadingStats>Carregando...</ng-template>
      </div>

      <div class="card">
        <h3>Adicionar sessão manual</h3>
        <form [formGroup]="manualForm" (ngSubmit)="saveManual()">
          <label>Minutos estudados</label>
          <input type="number" formControlName="minutes" min="1" />
          <button class="btn" type="submit" [disabled]="manualForm.invalid">Salvar</button>
        </form>
      </div>

      <div class="card">
        <h3>Timer rápido</h3>
        <p *ngIf="!timerRunning()">Clique para iniciar uma sessão cronometrada.</p>
        <p *ngIf="timerRunning()">Tempo decorrido: <strong>{{ elapsedSeconds() }}</strong> segundos</p>
        <button class="btn" (click)="toggleTimer()">
          {{ timerRunning() ? 'Encerrar sessão' : 'Iniciar timer' }}
        </button>
      </div>

      <div class="card">
        <h3>Meta diária</h3>
        <form [formGroup]="goalForm" (ngSubmit)="saveGoal()">
          <label>Minutos desejados</label>
          <input type="number" formControlName="target" min="1" />
          <button class="btn" type="submit" [disabled]="goalForm.invalid">Atualizar meta</button>
        </form>
      </div>

      <div class="card">
        <h3>Sessões de hoje</h3>
        <p *ngIf="sessions().length === 0">Nenhuma sessão registrada ainda.</p>
        <ul>
          <li *ngFor="let session of sessions()">
            {{ session.durationMinutes }} min —
            <span>{{ session.manualEntry ? 'Manual' : 'Cronômetro' }}</span>
            <small *ngIf="session.startedAt"> • início {{ session.startedAt | date: 'shortTime' }}</small>
          </li>
        </ul>
      </div>
    </div>
  `,
  styles: [
    `
      .stats-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
        gap: 1rem;
      }

      ul {
        padding: 0;
        list-style: none;
      }

      li {
        padding: 0.5rem 0;
        border-bottom: 1px solid #e5e7eb;
      }
    `
  ]
})
export class TodayComponent implements OnInit, OnDestroy {
  private readonly api = inject(ApiService);
  private readonly fb = inject(FormBuilder);

  readonly sessions = signal<StudySession[]>([]);
  readonly stats = signal<StatsResponse | null>(null);
  readonly timerRunning = signal(false);
  readonly elapsedSeconds = signal(0);

  manualForm = this.fb.group({
    minutes: [25, [Validators.required, Validators.min(1)]]
  });

  goalForm = this.fb.group({
    target: [0, [Validators.required, Validators.min(1)]]
  });

  private timerInterval: any;
  private timerStart: number | null = null;

  ngOnInit(): void {
    this.loadData();
  }

  ngOnDestroy(): void {
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
    }
  }

  private loadData(): void {
    this.api.getTodaySessions().subscribe((sessions) => this.sessions.set(sessions));
    this.api.getTodayStats().subscribe((stats) => {
      this.stats.set(stats);
      this.goalForm.patchValue({ target: stats.goalTargetMinutes || 0 });
    });
  }

  saveManual(): void {
    if (this.manualForm.invalid) {
      return;
    }
    const minutes = this.manualForm.value.minutes!;
    const end = new Date();
    const start = new Date(end.getTime() - minutes * 60_000);
    this.api
      .createSession({
        manualEntry: true,
        durationMinutes: minutes,
        startedAt: start.toISOString()
      })
      .subscribe(() => this.loadData());
    this.manualForm.reset({ minutes: 25 });
  }

  toggleTimer(): void {
    if (this.timerRunning()) {
      this.stopTimer();
    } else {
      this.startTimer();
    }
  }

  private startTimer(): void {
    this.timerStart = Date.now();
    this.timerRunning.set(true);
    this.elapsedSeconds.set(0);
    this.timerInterval = setInterval(() => {
      if (this.timerStart) {
        const seconds = Math.floor((Date.now() - this.timerStart) / 1000);
        this.elapsedSeconds.set(seconds);
      }
    }, 1000);
  }

  private stopTimer(): void {
    if (!this.timerStart) {
      return;
    }
    const end = Date.now();
    const durationMinutes = Math.max(1, Math.round((end - this.timerStart) / 60000));
    const startDate = new Date(this.timerStart);
    const endDate = new Date(end);
    this.api
      .createSession({
        manualEntry: false,
        startedAt: startDate.toISOString(),
        endedAt: endDate.toISOString(),
        durationMinutes
      })
      .subscribe(() => this.loadData());
    this.timerRunning.set(false);
    this.elapsedSeconds.set(0);
    this.timerStart = null;
    clearInterval(this.timerInterval);
  }

  saveGoal(): void {
    if (this.goalForm.invalid) {
      return;
    }
    const target = this.goalForm.value.target!;
    this.api.updateDailyGoal(target).subscribe(() => this.loadData());
  }
}
