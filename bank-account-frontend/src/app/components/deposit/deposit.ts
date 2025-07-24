import { Component } from '@angular/core';
import { AccountService } from '../../services/account';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputValidatorService } from '../../services/input-validator';

@Component({
  selector: 'app-deposit',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './deposit.html',
  styleUrls: ['./deposit.scss']
})
export class Deposit {
  amount: number = 0;
  message = '';

  constructor(private accountService: AccountService, private inputValidator: InputValidatorService) {}

  validateNumber(event: KeyboardEvent) {
    this.inputValidator.validateNumber(event); // Utilisation du service
  }

  blockPaste(event: ClipboardEvent) {
    this.inputValidator.blockPaste(event); // Utilisation du service
  }

  deposit() {
    if (!Number.isFinite(this.amount) || this.amount <= 0) {
      this.message = "Veuillez entrer un montant valide supérieur à 0.";
      return;
    }

    this.accountService.deposit(this.amount).subscribe({
      next: (res) => this.message = `Dépôt de ${res.amount}€ effectué.`,
      error: () => this.message = `Erreur lors du dépôt.`
    });
  }
}