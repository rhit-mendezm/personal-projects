import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TitleScreenComponent } from './title-screen.component';

describe('TitleScreenComponent', () => {
  let component: TitleScreenComponent;
  let fixture: ComponentFixture<TitleScreenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TitleScreenComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TitleScreenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
