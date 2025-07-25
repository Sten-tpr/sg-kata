import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Statement } from './statement';
import { AccountService } from '../../services/account';
import { of } from 'rxjs';

describe('Statement', () => {
  let component: Statement;
  let fixture: ComponentFixture<Statement>;
  let mockAccountService: jasmine.SpyObj<AccountService>;

  beforeEach(async () => {
    mockAccountService = jasmine.createSpyObj('AccountService', ['getStatement']);

    await TestBed.configureTestingModule({
      imports: [Statement],
      providers: [{ provide: AccountService, useValue: mockAccountService }]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Statement);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call getStatement and set transactions on init', () => {
    const dummyTransactions = [
      { id: 1, date: '2025-07-25T00:00:00Z', amount: 100, balance: 1000 },
    ];
    mockAccountService.getStatement.and.returnValue(of(dummyTransactions));

    fixture.detectChanges(); // ngOnInit called

    expect(mockAccountService.getStatement).toHaveBeenCalled();
    expect(component.transactions).toEqual(dummyTransactions);
  });
});