import { Component, OnInit } from '@angular/core';
import { Transaction } from '../../models/transaction';
import { Account } from '../../services/account';
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

  constructor(private accountService: Account) {}

  ngOnInit(): void {
    this.accountService.getStatement().subscribe(data => {
      this.transactions = data;
    });
  }
}
