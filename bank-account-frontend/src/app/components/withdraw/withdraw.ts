import { Component } from '@angular/core';
import { Account } from '../../services/account';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-withdraw',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './withdraw.html',
  styleUrl: './withdraw.scss'
})
export class Withdraw {
amount = 0;
  message = '';

  constructor(private bankService: Account) {}

  withdraw(): void {
    this.bankService.withdraw(this.amount).subscribe({
      next: () => this.message = 'Retrait effectué.',
      error: () => this.message = 'Erreur lors du retrait.'
    });
  }
}
