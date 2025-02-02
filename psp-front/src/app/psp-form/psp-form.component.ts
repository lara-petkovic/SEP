import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';

interface Card {
  name: string;
  id: string;
}

@Component({
  selector: 'app-psp-form',
  templateUrl: './psp-form.component.html',
  styleUrls: ['./psp-form.component.css']
})
export class PspFormComponent implements OnInit{
  @Output() paymentMethodSelected = new EventEmitter<{ name: string; orderid: string | null; merchantid: string | null }>();

  constructor(private router: Router){}
  selectedRow: any = null;
  header: string = "Choose one payment method";
  cards: Card[] = [];

  ngOnInit(): void {
    this.selectedRow = null;
  }

  private _paymentOptions: string = '';

  @Input()
  set paymentOptions(value: string) {
    this._paymentOptions = value;
    this.updateCards(); 
  }

  get paymentOptions(): string {
    return this._paymentOptions;
  }

  displayedColumns: string[] = ['name'];

  selectCard(card: any) {
    if(card.name!=='Card'){
      alert('Payment option not supported yet.');
      this.selectedRow = null;
      return;
    }
    if (this.selectedRow === card) {
      return;
    }
    this.selectedRow = card;
    alert('chosen payment method : '+card.name);
    const jsonString = this._paymentOptions
    .replace(/'/g, '"')
    .replace(/(\w+)=/g, '"$1":');
    const data = JSON.parse(jsonString);
    this.paymentMethodSelected.emit({ name: card.name, orderid: data.orderId, merchantid: data.merchantId });
    this.selectedRow = null;
  }

  private updateCards() {
    this.cards = []; 
    if (this.paymentOptions.includes("Card")) this.cards.push({ name: 'Card', id: '1' });
    if (this.paymentOptions.includes("QR")) this.cards.push({ name: 'QR Code', id: '2' });
    if (this.paymentOptions.includes("PayPal")) this.cards.push({ name: 'PayPal', id: '3' });
    if (this.paymentOptions.includes("Bitcoin")) this.cards.push({ name: 'Bitcoin', id: '4' });
    // this.paymentOptions.split(',').forEach((option, index) => {
    //   const trimmedOption = option.trim(); 
    //   if (trimmedOption) {
    //     this.cards.push({ name: trimmedOption, id: (index + 1).toString() });
    //   }
    // });
  }

  getFilteredCards() {
    return this.selectedRow ? [this.selectedRow] : this.cards;
  }
}
