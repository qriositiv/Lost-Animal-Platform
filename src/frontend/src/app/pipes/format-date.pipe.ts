import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formatDateArray',
  standalone: true,
})
export class FormatDateArrayPipe implements PipeTransform {
  transform(dateArray: number[]): string;
  transform(dateString: string): string;

  transform(value: number[] | string): string {
    if (Array.isArray(value)) {
      if (value.length < 7) return '';
      const [year, month, day, hours, minutes, seconds, milliseconds] = value;
      const correctMilliseconds = Math.floor(milliseconds / 1000000);
      const date = new Date(year, month - 1, day, hours, minutes, seconds, correctMilliseconds);
      return date.toLocaleString();
    } else if (typeof value === 'string') {
      const dateArray = value.split(',').map(Number);
      if (dateArray.length < 7) return '';
      const [year, month, day, hours, minutes, seconds, milliseconds] = dateArray;
      const correctMilliseconds = Math.floor(milliseconds / 1000000);
      const date = new Date(year, month - 1, day, hours, minutes, seconds, correctMilliseconds);
      return date.toLocaleString();
    }
    return '';
  }
}

@Pipe({
  name: 'shortDate',
  standalone: true,
})
export class FormatShortDatePipe implements PipeTransform {
  transform(date: string): string {
    if (!date || typeof date !== 'string') {
      return '';
    }

    const dateTimeParts = date.split(',');
    if (dateTimeParts.length === 0) {
      return '';
    }

    return dateTimeParts[0].trim();
  }
}
