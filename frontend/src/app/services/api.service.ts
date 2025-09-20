import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface StudySession {
  id: number;
  subjectId: number | null;
  topicId: number | null;
  startedAt: string;
  endedAt: string | null;
  durationMinutes: number;
  manualEntry: boolean;
}

export interface Question {
  id: number;
  prompt: string;
  answer?: string;
  subjectId?: number;
  topicId?: number;
  createdAt: string;
}

export interface Attempt {
  id: number;
  questionId: number;
  correct: boolean;
  notes?: string;
  attemptedAt: string;
}

export interface StatsResponse {
  totalStudyMinutes: number;
  sessionCount: number;
  attempts: number;
  correctAttempts: number;
  goalTargetMinutes: number;
  goalProgressMinutes: number;
}

export interface DailyGoalResponse {
  date: string;
  targetMinutes: number;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly apiUrl = 'http://localhost:8080';

  constructor(private readonly http: HttpClient) {}

  getTodaySessions(): Observable<StudySession[]> {
    return this.http.get<StudySession[]>(`${this.apiUrl}/sessions/today`);
  }

  createSession(payload: Partial<StudySession> & { startedAt: string; manualEntry: boolean }): Observable<StudySession> {
    return this.http.post<StudySession>(`${this.apiUrl}/sessions`, payload);
  }

  getQuestions(): Observable<Question[]> {
    return this.http.get<Question[]>(`${this.apiUrl}/questions`);
  }

  createQuestion(payload: { prompt: string; answer?: string; subjectId?: number; topicId?: number }): Observable<Question> {
    return this.http.post<Question>(`${this.apiUrl}/questions`, payload);
  }

  createAttempt(questionId: number, payload: { correct: boolean; notes?: string }): Observable<Attempt> {
    return this.http.post<Attempt>(`${this.apiUrl}/questions/${questionId}/attempts`, payload);
  }

  getTodayStats(): Observable<StatsResponse> {
    return this.http.get<StatsResponse>(`${this.apiUrl}/stats/today`);
  }

  getWeekStats(): Observable<StatsResponse> {
    return this.http.get<StatsResponse>(`${this.apiUrl}/stats/week`);
  }

  getDailyGoal(): Observable<DailyGoalResponse> {
    return this.http.get<DailyGoalResponse>(`${this.apiUrl}/daily-goal`);
  }

  updateDailyGoal(targetMinutes: number): Observable<DailyGoalResponse> {
    return this.http.post<DailyGoalResponse>(`${this.apiUrl}/daily-goal`, { targetMinutes });
  }
}
