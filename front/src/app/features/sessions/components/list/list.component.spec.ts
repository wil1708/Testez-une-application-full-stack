import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterTestingModule } from '@angular/router/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { SessionApiService } from '../../services/session-api.service';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockSessionApiService = {
    all: () => of([
      { id: 1, name: 'Session matin', date: new Date(), description: 'Yoga le matin' },
      { id: 2, name: 'Session soir', date: new Date(), description: 'Yoga le soir' }
    ])
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatButtonModule,
        RouterTestingModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Unit tests

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should display the create button if user is admin', () => {
    const button = fixture.debugElement.query(By.css('button[routerLink="create"]'));
    expect(button).toBeTruthy();
  });

  it('should display the correct number of sessions', () => {
    const sessionElements = fixture.debugElement.queryAll(By.css('.item'));
    expect(sessionElements.length).toBe(2);
  });

  it('should set the correct router link for the detail button', () => {
    const button = fixture.debugElement.queryAll(By.css('button'))[1].nativeElement;
    expect(button.textContent).toContain('Detail');
  });

  it('should set the correct router link for the edit button if user is admin', () => {
    const button = fixture.debugElement.queryAll(By.css('button'))[2].nativeElement;
    expect(button.textContent).toContain('Edit');
  });

  // Integration tests

  it('should display session details correctly', () => {
    const sessionElement = fixture.debugElement.query(By.css('.item'));
    const sessionTitle = sessionElement.query(By.css('mat-card-title')).nativeElement;
    const sessionSubtitle = sessionElement.query(By.css('mat-card-subtitle')).nativeElement;
    const sessionDescription = sessionElement.query(By.css('mat-card-content')).nativeElement;
    
    expect(sessionTitle.textContent).toContain('Session matin');
    expect(sessionSubtitle.textContent).toContain('Session on');
    expect(sessionDescription.textContent).toContain('Yoga le matin');
  });

  
});
