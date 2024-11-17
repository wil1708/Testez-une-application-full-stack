import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: any;
  let sessionService: any;
  let router: Router;

  beforeEach(async () => {
    authService = {
      login: jest.fn()
    };
    sessionService = {
      logIn: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: SessionService, useValue: sessionService },
        {
          provide: Router,
          useValue: {
            navigate: jest.fn()
          }
        }
      ],
      imports: [
        RouterTestingModule.withRoutes([]),
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  // Unit tests

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should create form with email and password controls', () => {
    expect(component.form.contains('email')).toBeTruthy();
    expect(component.form.contains('password')).toBeTruthy();
  });

  it('should make the email control required and validate email', () => {
    const emailControl = component.form.get('email');
    emailControl?.setValue('');
    expect(emailControl?.valid).toBeFalsy();

    emailControl?.setValue('not-a-valid-email');
    expect(emailControl?.valid).toBeFalsy();

    emailControl?.setValue('test@example.com');
    expect(emailControl?.valid).toBeTruthy();
  });

  it('should make the password control required and validate minimum length', () => {
    const passwordControl = component.form.get('password');
    passwordControl?.setValue('');
    expect(passwordControl?.valid).toBeFalsy();

    passwordControl?.setValue('12');
    expect(passwordControl?.valid).toBeTruthy();

    passwordControl?.setValue('123');
    expect(passwordControl?.valid).toBeTruthy();
  });

  // Integration tests

  it('should call login method on submit when form is valid', () => {
    authService.login.mockReturnValue(of({} as SessionInformation)); 
    component.form.setValue({
      email: 'test@example.com',
      password: '12345'
    });
    fixture.detectChanges();

    component.submit();
    expect(authService.login).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: '12345'
    });
  });

  it('should show error message on login failure', () => {
    authService.login.mockReturnValue(throwError({ error: 'An error occurred' })); 
    component.form.setValue({
      email: 'yoga@studio.com',
      password: '12345'
    });
    fixture.detectChanges();

    component.submit();
    fixture.detectChanges(); 

    const errorMessage = fixture.nativeElement.querySelector('.error');
    expect(component.onError).toBeTruthy();
    expect(errorMessage).toBeTruthy();
    expect(errorMessage.textContent).toContain('An error occurred');
  });

  it('should navigate to sessions on successful login', () => {
    const routerSpy = jest.spyOn(router, 'navigate'); 
    authService.login.mockReturnValue(of({} as SessionInformation));

    component.form.setValue({
      email: 'yoga@studio.com',
      password: 'test!1234'
    });
    fixture.detectChanges();
    component.submit();
    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
  });
});
