import { ImageCropperComponent, ImageCroppedEvent } from 'ngx-image-cropper';
import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-image-upload-ngx',
  standalone: true,
  imports: [ImageCropperComponent],
  templateUrl: './image-upload-ngx.component.html',
})
export class ImageUploadComponentNgx {
  @Output() imageCroppedEvent: EventEmitter<string> = new EventEmitter<string>();
  imageChangedEvent: Event | null = null;
  croppedImage: string = '';
  showCropped: boolean = false;
  squareSize = 600;
  imageQuality = 0.7;

  onDragOver(event: DragEvent) {
    event.preventDefault(); // Necessary to allow the drop
    event.stopPropagation();
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    const files = event.dataTransfer?.files;
    if (files && files.length > 0) {
      this.handleFileInput({ target: { files: files } });
    }
  }

  handleFileInput(event: any) {
    this.imageChangedEvent = event;
  }

  imageCropped(event: ImageCroppedEvent) {
    if (event.objectUrl) {
      this.croppedImage = event.objectUrl;
    } else {
      console.error('Object URL is undefined');
    }
  }

  clearImage() {
    this.imageChangedEvent = null;
    this.croppedImage = '';
    this.showCropped = false;
  }

  finishAndCrop() {
    this.showCropped = true;
    this.imageCroppedEvent.emit(this.croppedImage);
  }
}
