import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, StatsResponse } from '../../services/api.service';
import { NgApexchartsModule } from 'ng-apexcharts';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule, NgApexchartsModule],
  template: `
    <div class="container">
      <div class="card">
        <h2>Últimos 7 dias</h2>
        <div class="grid">
          <div>
            <h3>Resumo semanal</h3>
            <p>Total estudado: <strong>{{ stats()?.totalStudyMinutes ?? 0 }} min</strong></p>
            <p>Sessões: <strong>{{ stats()?.sessionCount ?? 0 }}</strong></p>
            <p>Questões respondidas: <strong>{{ stats()?.attempts ?? 0 }}</strong></p>
            <p>Acertos: <strong>{{ stats()?.correctAttempts ?? 0 }}</strong></p>
          </div>
          <div>
            <apx-chart
              *ngIf="chartSeries().length"
              [series]="chartSeries()"
              [chart]="{ type: 'donut' }"
              [labels]="['Acertos', 'Erros']"
              [colors]="['#22c55e', '#ef4444']"
            ></apx-chart>
            <p *ngIf="!chartSeries().length">Sem dados de tentativas na última semana.</p>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
        gap: 2rem;
        align-items: center;
      }
    `
  ]
})
export class HistoryComponent implements OnInit {
  private readonly api = inject(ApiService);

  readonly stats = signal<StatsResponse | null>(null);
  readonly chartSeries = signal<number[]>([]);

  ngOnInit(): void {
    this.api.getWeekStats().subscribe((stats) => {
      this.stats.set(stats);
      const errors = Math.max(0, stats.attempts - stats.correctAttempts);
      if (stats.attempts > 0) {
        this.chartSeries.set([stats.correctAttempts, errors]);
      } else {
        this.chartSeries.set([]);
      }
    });
  }
}
