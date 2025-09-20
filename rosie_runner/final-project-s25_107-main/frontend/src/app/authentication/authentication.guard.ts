import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authenticationGuard: CanActivateFn = () => {
  const router = inject(Router);

  if (sessionStorage.getItem('username')) {
    return true;
  } else {
    router.navigate(['/']);
    return false;
  }
};