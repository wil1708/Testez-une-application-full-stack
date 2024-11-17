import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { DetailComponent } from './detail.component';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of({
      id: '1',
      name: 'Session Yoga',
      date: new Date(),
      users: [1],
      description: 'Une session Yoga apaisement.',
      teacher_id: 1,
      createdAt: new Date(),
      updatedAt: new Date()
    })),
    delete: jest.fn().mockReturnValue(of({})),
    participate: jest.fn().mockReturnValue(of({})),
    unParticipate: jest.fn().mockReturnValue(of({}))
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of({
      id: '1',
      firstName: 'William',
      lastName: 'Pires'
    }))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatIconModule,
        MatCardModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Unit tests
  
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session on init', () => {
    component.ngOnInit();
    expect(mockSessionApiService.detail).toHaveBeenCalledWith(component.sessionId);
    expect(component.session).toEqual(expect.objectContaining({
      id: '1',
      name: 'Session Yoga',
      description: 'Une session Yoga apaisement.',
      teacher_id: 1,
      users: [1],
    }));
  });

  it('should fetch teacher on session fetch', () => {
    component.ngOnInit();
    expect(mockTeacherService.detail).toHaveBeenCalledWith('1');
    expect(component.teacher).toEqual({
      id: '1',
      firstName: 'William',
      lastName: 'Pires'
    });
  });

  it('should delete session', () => {
    component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalledWith(component.sessionId);
  });

  it('should participate in session', () => {
    component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
  });

  it('should unparticipate from session', () => {
    component.unParticipate();
    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
  });
});
