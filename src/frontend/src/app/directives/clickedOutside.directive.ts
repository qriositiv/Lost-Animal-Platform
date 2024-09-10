import {
  Directive,
  ElementRef,
  Output,
  EventEmitter,
  HostListener, OnInit
} from "@angular/core";

@Directive({
  selector: '[clickedOutside]',
  standalone: true,
})
export class clickedOutside implements OnInit {
  // Near the directive there should be a (clickOutside) event which gets triggered when the user clicks outside the directive
  @Output() clickOutside = new EventEmitter<void>();
  private listening = false;
  // Get the parent element of the directive
  constructor(private element: ElementRef) {}

  ngOnInit(): void {
    setTimeout(() => {
      this.listening = true;
    }, 500); // 500 milliseconds delay
  }

  @HostListener('document:click', ['$event.target'])
  public onDocumentClick(target: any): void {
    if (!this.listening) return;
    const clickedInside = this.element.nativeElement.contains(target);
    if (!clickedInside) {
      this.clickOutside.emit();
    }
  }
}
