import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  // Unit tests

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all sessions', () => {
    const dummySessions: Session[] = [
      { id: 1, name: 'Session matin', description: 'Yoga le matin', date: new Date(), teacher_id: 1, users: [], createdAt: new Date(), updatedAt: new Date() },
      { id: 2, name: 'Session soir', description: 'Yoga le soir', date: new Date(), teacher_id: 2, users: [], createdAt: new Date(), updatedAt: new Date() }
    ];

    service.all().subscribe(sessions => {
      expect(sessions.length).toBe(2);
      expect(sessions).toEqual(dummySessions);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(dummySessions);
  });

  it('should retrieve session detail', () => {
    const dummySession: Session = { id: 1, name: 'Session matin', description: 'Yoga le matin', date: new Date(), teacher_id: 1, users: [], createdAt: new Date(), updatedAt: new Date() };

    service.detail('1').subscribe(session => {
      expect(session).toEqual(dummySession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(dummySession);
  });

  it('should delete session', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should create a new session', () => {
    const newSession: Session = { id: 3, name: 'Session week-end', description: 'Yoga le week-end', date: new Date(), teacher_id: 3, users: [], createdAt: new Date(), updatedAt: new Date() };

    service.create(newSession).subscribe(session => {
      expect(session).toEqual(newSession);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    req.flush(newSession);
  });

  it('should update a session', () => {
    const updatedSession: Session = { id: 1, name: 'Updated Session 1', description: 'Updated Description', date: new Date(), teacher_id: 1, users: [], createdAt: new Date(), updatedAt: new Date() };

    service.update('1', updatedSession).subscribe(session => {
      expect(session).toEqual(updatedSession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    req.flush(updatedSession);
  });

  it('should participate in a session', () => {
    service.participate('1', 'user1').subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne('api/session/1/participate/user1');
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('should unparticipate from a session', () => {
    service.unParticipate('1', 'user1').subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne('api/session/1/participate/user1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
