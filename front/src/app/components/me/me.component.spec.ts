import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { MeComponent } from './me.component';
import { SessionService } from '../../services/session.service';
import { UserService } from '../../services/user.service';
import { User } from '../../interfaces/user.interface';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      id: 1,
      admin: true
    },
    logOut: jest.fn()
  };

  const mockUser: User = {
    id: 1,
    firstName: 'William',
    lastName: 'PIRES',
    email: 'william.pires@example.com',
    password: 'password123',
    admin: false,
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of(mockUser)),
    delete: jest.fn().mockReturnValue(of({})) 
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Unit tests

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display user information', () => {
    fixture.detectChanges();
    const userInfo = fixture.debugElement.query(By.css('mat-card-content')).nativeElement;
    expect(userInfo.textContent).toContain('William');
    expect(userInfo.textContent).toContain('PIRES');
    expect(userInfo.textContent).toContain('william.pires@example.com');
  });

  it('should show "Delete my account" button if user is not admin', () => {
    const deleteButton = fixture.debugElement.query(By.css('button[color="warn"]'));
    expect(deleteButton).toBeTruthy();
  });

  // Integration tests

  it('should call delete method and log out the user', async () => {
    const deleteButton = fixture.debugElement.query(By.css('button[color="warn"]')).nativeElement;

    deleteButton.click();
    fixture.detectChanges();

    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockMatSnackBar.open).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });

  it('should navigate back when back button is clicked', () => {
    jest.spyOn(window.history, 'back');
    const backButton = fixture.debugElement.query(By.css('button[mat-icon-button]')).nativeElement;
    backButton.click();
    fixture.detectChanges();
    expect(window.history.back).toHaveBeenCalled();
  });
});
