import { Component, OnInit } from '@angular/core';
import { Transaction } from '../../models/transaction';
import { AccountService } from '../../services/account';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-statement',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './statement.html',
  styleUrls: ['./statement.scss']
})
export class Statement implements OnInit {
  transactions: Transaction[] = [];

  constructor(private accountService: AccountService) {}

  ngOnInit(): void {
    this.accountService.getStatement().subscribe(data => {
      this.transactions = data;
    });
  }
}
