import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { expect } from '@jest/globals';

import { FormComponent } from './form.component';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { Session } from '../../interfaces/session.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
  };

  const mockSessionApiService = {
    create: jest.fn().mockReturnValue(of({})),
    update: jest.fn().mockReturnValue(of({})),
    detail: jest.fn().mockReturnValue(of({
      id: '1',
      name: 'Yoga Session',
      date: new Date().toISOString(),
      teacher_id: 1,
      description: 'A relaxing yoga session.',
      users: []
    })),
  };

  const mockTeacherService = {
    all: jest.fn().mockReturnValue(of([{ id: 1, firstName: 'John', lastName: 'Doe' }])),
  };

  const mockMatSnackBar = {
    open: jest.fn(),
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('1'),
      },
    },
  };

  const mockRouter = {
    navigate: jest.fn(),
    url: ''
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([]),
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: Router, useValue: mockRouter },
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  //Unit tests

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form on create mode', () => {
    mockRouter.url = '/create';
    component.ngOnInit();
    expect(component.onUpdate).toBeFalsy();
    expect(component.sessionForm?.valid).toBeFalsy();
    expect(component.sessionForm?.get('name')?.value).toBe('');
  });

  it('should initialize form on update mode', () => {
    mockRouter.url = '/update';
    component.ngOnInit();
    expect(component.onUpdate).toBeTruthy();
    expect(component.sessionForm?.get('name')?.value).toBe('Yoga Session');
  });

  // Integration tests
  
  it('should call create method on submit when form is valid and onUpdate is false', () => {
    mockRouter.url = '/create';
    component.ngOnInit();
    component.onUpdate = false;
    expect(component.sessionForm).toBeDefined();

    component.sessionForm?.setValue({
      name: 'Session Name',
      date: '2023-01-01',
      teacher_id: 1,
      description: 'Session Description',
    });
    
    expect(component.sessionForm?.valid).toBeTruthy();
    component.submit();
    fixture.detectChanges();
    expect(mockSessionApiService.create).toHaveBeenCalled();
  });  
  
  it('should call update method on submit when form is valid and onUpdate is true', () => {
    mockRouter.url = '/update';
    component.ngOnInit();
    component.sessionForm?.setValue({
      name: 'Session Name',
      date: '2023-01-01',
      teacher_id: 1,
      description: 'Session Description',
    });
    component.submit();
    fixture.detectChanges();
    expect(mockSessionApiService.update).toHaveBeenCalled();
  });

  it('should navigate to sessions after successful create', () => {
    mockRouter.url = '/create';
    component.ngOnInit();
    component.sessionForm?.setValue({
      name: 'Session Name',
      date: '2023-01-01',
      teacher_id: 1,
      description: 'Session Description',
    });
    component.submit();
    fixture.detectChanges(); 
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
  });

  it('should navigate to sessions after successful update', () => {
    mockRouter.url = '/update';
    component.ngOnInit();
    component.sessionForm?.setValue({
      name: 'Session Name',
      date: '2023-01-01',
      teacher_id: 1,
      description: 'Session Description',
    });
    component.submit();
    fixture.detectChanges(); 
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
  });
});


