import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisteredProductListComponent } from './registered-product-list.component';

describe('RegisteredProductListComponent', () => {
  let component: RegisteredProductListComponent;
  let fixture: ComponentFixture<RegisteredProductListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisteredProductListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisteredProductListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
