import { RouterModule, Routes } from "@angular/router";
import { Deposit } from "./components/deposit/deposit";
import { Withdraw } from "./components/withdraw/withdraw";
import { Statement } from "./components/statement/statement";

export const routes: Routes = [
  { path: '', redirectTo: 'statement', pathMatch: 'full' },
  { path: 'deposit', component: Deposit },
  { path: 'withdraw', component: Withdraw },
  { path: 'statement', component: Statement }
];