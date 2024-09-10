import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { LoaderService } from '../services/loader.service';

export const loadingInterceptor: HttpInterceptorFn = (request, next) => {
  const loadingService = inject(LoaderService);
  let totalRequests = 0;

  totalRequests++;
  loadingService.setLoading(true);

  return next(request).pipe(
    finalize(() => {
      totalRequests--;
      if (totalRequests === 0) {
        loadingService.setLoading(false);
      }
    }),
  );
};
