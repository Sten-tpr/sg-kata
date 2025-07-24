import { Component } from '@angular/core';
import { AccountService } from '../../services/account';
import { FormsModule } from '@angular/forms';
import { InputValidatorService } from '../../services/input-validator';

@Component({
  selector: 'app-withdraw',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './withdraw.html',
  styleUrls: ['./withdraw.scss']
})
export class Withdraw {
  amount = 0;
  message = '';

  constructor(private accountService: AccountService, private inputValidator: InputValidatorService) {}
  
    validateNumber(event: KeyboardEvent) {
      this.inputValidator.validateNumber(event); // Utilisation du service
    }
  
    blockPaste(event: ClipboardEvent) {
      this.inputValidator.blockPaste(event); // Utilisation du service
    }

  withdraw(): void {
    if (!Number.isFinite(this.amount) || this.amount <= 0) {
      this.message = "Veuillez entrer un montant valide supérieur à 0.";
      return;
    }

    this.accountService.withdraw(this.amount).subscribe({
      next: () => this.message = 'Retrait effectué.',
      error: () => this.message = 'Erreur lors du retrait.'
    });
  }
}