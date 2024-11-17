import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NotFoundComponent } from './not-found.component';
import { expect } from '@jest/globals';


describe('NotFoundComponent', () => {
  let component: NotFoundComponent;
  let fixture: ComponentFixture<NotFoundComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotFoundComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Unit tests

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render the title "Page not found !"', () => {
    const titleElement = fixture.nativeElement.querySelector('h1');
    expect(titleElement.textContent).toContain('Page not found !');
  });

  it('should have a class "flex justify-center mt3"', () => {
    const containerElement = fixture.nativeElement.querySelector('div');
    expect(containerElement.className).toBe('flex justify-center mt3');
  });
});
