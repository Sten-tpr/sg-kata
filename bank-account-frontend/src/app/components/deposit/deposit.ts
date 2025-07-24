import { Component } from '@angular/core';
import { Account } from '../../services/account';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-deposit',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './deposit.html',
  styleUrl: './deposit.scss'
})
export class Deposit {

  amount: number = 0;
  message = '';

  constructor(private accountService: Account) {}

  deposit() {
    this.accountService.deposit(this.amount).subscribe({
      next: (res) => this.message = `Dépôt de ${res.amount}€ effectué.`,
      error: () => this.message = `Erreur lors du dépôt.`
    });
  }

}
