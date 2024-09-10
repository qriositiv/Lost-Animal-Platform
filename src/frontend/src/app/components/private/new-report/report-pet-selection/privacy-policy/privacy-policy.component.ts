import { Component, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-privacy-policy',
  standalone: true,
  imports: [],
  templateUrl: './privacy-policy.component.html'
})
export class PrivacyPolicyComponent {
  @Output() closeModal: EventEmitter<void> = new EventEmitter<void>();

  close() {
    this.closeModal.emit();
  }
}
