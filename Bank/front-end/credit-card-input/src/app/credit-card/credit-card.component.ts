import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-credit-card',
  templateUrl: './credit-card.component.html',
  styleUrls: ['./credit-card.component.css']
})
export class CreditCardComponent {
  cardDetails = {
    number: '',
    holder: '',
    expiry: '',
    ccv: ''
  };
  cardDetailsDto = {
    number: '',
    holder: '',
    expiry: '',
    ccv: ''
  }

  formatExpiryDate(event: Event): void {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/[^\d]/g, '');

    if (value.length > 4) {
      value = value.slice(0, 4);
    }

    if (value.length >= 3) {
      value = value.slice(0, 2) + '/' + value.slice(2);
    }

    input.value = value;
  }
  formatCardNumber(event: Event): void {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/\D/g, '');  // Remove any non-digit characters

    let formattedValue = '';
    for (let i = 0; i < value.length; i += 4) {
      if (i + 4 <= value.length) {
        formattedValue += value.slice(i, i + 4) + ' ';
      } else {
        formattedValue += value.slice(i);
      }
    }
    formattedValue = formattedValue.trim();

    input.value = formattedValue;
  }

  handleBackspace(event: KeyboardEvent): void {
    const input = event.target as HTMLInputElement;
    if (event.key === 'Backspace' && input.selectionEnd === input.value.length) {
      const lastChar = input.value.charAt(input.value.length - 1);
      if (lastChar === ' ') {
        input.value = input.value.slice(0, -1); 
      }
    }
  }

  submit(form:NgForm): void {
    this.cardDetailsDto.number = this.cardDetails.number.replace(/\s+/g, '');
    this.cardDetailsDto.ccv = this.cardDetails.ccv;
    this.cardDetailsDto.holder = this.cardDetails.holder;
    this.cardDetailsDto.expiry = this.cardDetails.expiry;
    if (form.valid) {
      alert(this.cardDetailsDto.number);
    } else {
      form.form.markAllAsTouched();
    }
  }
  isValidCardNumber(cardNumber: string): boolean {
    cardNumber = cardNumber.replace(/\D/g, '');
    let sum = 0;
    let shouldDouble = false;  
    for (let i = cardNumber.length - 1; i >= 0; i--) {
      let digit = parseInt(cardNumber.charAt(i));  
      if (shouldDouble) {
        digit *= 2;
        if (digit > 9) {
          digit -= 9;
        }
      }
      sum += digit;
      shouldDouble = !shouldDouble;
    }
    return sum % 10 === 0;
  }
  
}
