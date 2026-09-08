import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { ReservationService } from '../services/reservation.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  username = '';
  password = '';
  error = '';
 
  constructor(private reservationService : ReservationService, private auth: AuthService, private router: Router) {}
  showSearchForm = false 

login() {
  if(!this.username.trim() || !this.password.trim()) {  
    this.error = 'Username and password are required!!!';
    return;
  }
  this.auth.login({ username: this.username, password: this.password }).subscribe({
    next: () => {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const payload = JSON.parse(atob(token.split('.')[1]));
          const role = payload.role;

          if (role === 'ROLE_ADMIN') {
            this.router.navigate(['/admin-home']);
          } else if (role === 'ROLE_PASSENGER') {
            
              this.router.navigate(['/user-home/passenger']);

          } else {
            this.router.navigate(['/unauthorized']);
          }
        } catch (e) {
          console.error('Invalid token', e);
          this.router.navigate(['/unauthorized']);
        }
      } else {
        this.router.navigate(['/unauthorized']);
      }
    },
    error: () => {
      this.error = 'Invalid username or password';
      // alert(this.error);
    }
  });
}
  goToRegister() {
    this.router.navigate(['/register']);      
  }
}
