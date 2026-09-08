import { Component, HostListener, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TrainService } from '../services/train.service';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';


@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.css']
})
export class UserHomeComponent implements OnInit{

  
  searchForm!: FormGroup;

  currentYear = new Date().getFullYear();

  constructor(private fb: FormBuilder, private trainService: TrainService, private router: Router, private authService : AuthService) {}
  
ngOnInit(): void {
    this.searchForm = this.fb.group({
      source: ['', Validators.required],
      destination: ['', Validators.required],
      classType: ['', Validators.required],
      date: ['', Validators.required]
    });
  }

  
showSearchForm = true;

submitForm(): void {
  if (this.searchForm.valid) {
    this.trainService.updateSearchParams(this.searchForm.value);
    this.showSearchForm = false; // hide the form
    this.router.navigate(['/user-home/trainlist']);
  }
}


get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }


  


  childRouteActive = false;

  onChildActivate() {
    this.childRouteActive = true;
  }

  onChildDeactivate() {
    this.childRouteActive = false;
  }


  
showFooter = !this.childRouteActive;

  @HostListener('window:scroll', [])
  onWindowScroll() {
    const scrollTop = window.scrollY;
    const windowHeight = window.innerHeight;
    const bodyHeight = document.body.offsetHeight;

    this.showFooter = scrollTop + windowHeight >= bodyHeight - 5;
  }


logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }


showMyReservations(){
  this.router.navigate(['/user-home/my-reservations']);
}

}
