import { Component, EventEmitter, Input, Output } from "@angular/core";
import { clickedOutside } from "../../../directives/clickedOutside.directive";
import { faTimes } from "@fortawesome/free-solid-svg-icons";
import { FaIconComponent } from "@fortawesome/angular-fontawesome";

@Component({
  selector: 'app-confirmation-popup',
  standalone: true,
  imports: [
    clickedOutside,
    FaIconComponent
  ],
  templateUrl: './confirmation-popup.component.html'
})
export class ConfirmationPopupComponent {
  @Output() closeModal: EventEmitter<void> = new EventEmitter<void>();
  @Output() confirmDeletion: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Input() petId: string | undefined;

  deleteItem() {
    this.confirmDeletion.emit(true);
    this.closeModal.emit();
  }

  onClickedOutside(): void {
    this.closeModal.emit();
  }

  protected readonly faTimes = faTimes;

}
