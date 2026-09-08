import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  user = {  username: '', password: '' };
  confirmPassword: string = '';
  passwordMismatch: boolean = false;
  constructor(private auth: AuthService, private router: Router) { }
 
  register() {
 
    if (this.user.password !== this.confirmPassword) {
      this.passwordMismatch = true;
      return;
    }
 
    this.passwordMismatch = false;
 
    const payload = {
      username: this.user.username,
      password: this.user.password
        };
    this.auth.register(payload).subscribe(() => this.router.navigate(['/login']));
  }


  goToLogin() {
    this.router.navigate(['/login']);
  }
  
}
