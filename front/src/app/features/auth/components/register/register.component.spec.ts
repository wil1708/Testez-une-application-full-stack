import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { RegisterComponent } from './register.component';
import { Router } from '@angular/router';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: any;
  let router: any;

  beforeEach(async () => {
    authService = {
      register: jest.fn()
    };

    router = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Unit tests

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should create form with email, firstName, lastName and password controls', () => {
    expect(component.form.contains('email')).toBeTruthy();
    expect(component.form.contains('firstName')).toBeTruthy();
    expect(component.form.contains('lastName')).toBeTruthy();
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

  it('should make the firstName and lastName controls required and validate value', () => {
    const firstNameControl = component.form.get('firstName');
    const lastNameControl = component.form.get('lastName');

    firstNameControl?.setValue('');
    expect(firstNameControl?.valid).toBeFalsy();

    firstNameControl?.setValue('2'); 
    expect(firstNameControl?.valid).toBeFalsy();

    firstNameControl?.setValue('3'); 
    expect(firstNameControl?.valid).toBeTruthy();

    firstNameControl?.setValue('21'); 
    expect(firstNameControl?.valid).toBeFalsy();

    lastNameControl?.setValue('');
    expect(lastNameControl?.valid).toBeFalsy();

    lastNameControl?.setValue('2'); 
    expect(lastNameControl?.valid).toBeFalsy();

    lastNameControl?.setValue('3'); 
    expect(lastNameControl?.valid).toBeTruthy();

    lastNameControl?.setValue('21'); 
    expect(lastNameControl?.valid).toBeFalsy();
  });

  it('should make the password control required and validate length', () => {
    const passwordControl = component.form.get('password');
    passwordControl?.setValue('');
    expect(passwordControl?.valid).toBeFalsy();

    passwordControl?.setValue('2'); 
    expect(passwordControl?.valid).toBeFalsy();

    passwordControl?.setValue('3'); 
    expect(passwordControl?.valid).toBeTruthy();

    passwordControl?.setValue('41'); 
    expect(passwordControl?.valid).toBeFalsy();
  });

  // Integration tests

  it('should call register method on submit when form is valid', () => {
    authService.register.mockReturnValue(of(void 0)); 
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'William',
      lastName: 'Pires',
      password: '12345'
    });
    fixture.detectChanges();
    component.submit();

    expect(authService.register).toHaveBeenCalledWith({
      email: 'test@example.com',
      firstName: 'William',
      lastName: 'Pires',
      password: '12345'
    });
  });

  it('should show error message on register failure', async () => { 
    authService.register.mockReturnValue(throwError( {error: 'Registration error' })); 

    component.form.setValue({ email: 'test@example.com', firstName: 'John', lastName: 'Doe', password: 'password' }); 

    component.submit(); fixture.detectChanges(); 

    const errorMessage = fixture.nativeElement.querySelector('.error'); expect(component.onError).toBeTruthy(); expect(errorMessage).toBeTruthy(); expect(errorMessage.textContent).toContain('An error occurred'); });
  

  it('should navigate to login on successful registration', () => {
    const routerSpy = jest.spyOn(router, 'navigate'); 
    authService.register.mockReturnValue(of(void 0));
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: '12345'
    });
    fixture.detectChanges();
    component.submit();

    expect(routerSpy).toHaveBeenCalledWith(['/login']);
  });
});
