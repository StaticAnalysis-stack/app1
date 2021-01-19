import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserProductOrdersComponent } from './user-product-orders.component';

describe('UserProductOrdersComponent', () => {
  let component: UserProductOrdersComponent;
  let fixture: ComponentFixture<UserProductOrdersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserProductOrdersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserProductOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
