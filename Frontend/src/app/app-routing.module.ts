import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AddTrainComponent } from './add-train/add-train.component';
import { AdminHomeComponent } from './admin-home/admin-home.component';
import { AddFareComponent } from './add-fare/add-fare.component';
import { AddSeatComponent } from './add-seat/add-seat.component';
import { AddCoachComponent } from './add-coach/add-coach.component';
import { UserHomeComponent } from './user-home/user-home.component';
import { ReservationsComponent } from './reservations-details/reservations.component';
import { TrainlistComponent } from './trainlist/trainlist.component';
import { PassengerFormComponent } from './passenger-form/passenger-form.component';
import { PaymentComponent } from './payment/payment.component';
import { ShowTicketComponent } from './show-ticket/show-ticket.component';
import { PassengerListComponent } from './passenger-list/passenger-list.component';
import { ManageTrainComponent } from './manage-train/manage-train.component';
import { ManageFareComponent } from './manage-fare/manage-fare.component';
import { ManageCoachComponent } from './manage-coach/manage-coach.component';
import { EditTrainComponent } from './edit-train/edit-train.component';
import { EditSeatComponent } from './edit-seat/edit-seat.component';
import { EditCoachComponent } from './edit-coach/edit-coach.component';
import { EditFareComponent } from './edit-fare/edit-fare.component';
import { ManageSeatComponent } from './manage-seat/manage-seat.component';
import { MyReservationComponent } from './my-reservation/my-reservation.component';
import { MakePaymentComponent } from './make-payment/make-payment.component';

const routes: Routes = [
  {path:'login', component:LoginComponent},
  {path:'register', component:RegisterComponent},
  {path: 'admin-home', component: AdminHomeComponent, 
    children:[
        {path:'addTrain', component:AddTrainComponent},
        {path:'addFare', component:AddFareComponent},
        {path:'addSeat', component:AddSeatComponent},
        {path: 'addCoach', component: AddCoachComponent},
        {path:'reservations', component:ReservationsComponent},
        {path: 'passenger-list', component: PassengerListComponent},
        {path:'manage-train-details', component:ManageTrainComponent},
        {path: 'manage-fare-details', component:ManageFareComponent},
        {path: 'manage-coach-details', component: ManageCoachComponent},
        {path: 'manage-seat-details', component:ManageSeatComponent},
        {path: 'edit-train/:trainId', component: EditTrainComponent},
        {path: 'edit-seat/:id', component: EditSeatComponent},
        {path: 'edit-coach/:coachId', component: EditCoachComponent},
        {path: 'edit-fare/:id', component: EditFareComponent}
    ]
  },
  {path:'unauthorized', redirectTo:'/login', pathMatch:"full"}, // Placeholder for unauthorized access
  {path:'user-home', component:UserHomeComponent, 
    children:[
      {path:'trainlist', component: TrainlistComponent},
      {path: 'passenger', component: PassengerFormComponent},
      {path: 'payment', component: PaymentComponent},
      {path: 'show-ticket', component:ShowTicketComponent},
      {path: 'my-reservations', component: MyReservationComponent},
      {path: 'make-payment', component: MakePaymentComponent},
    ]
  },
  
  {path:'',redirectTo:'/user-home', pathMatch:'full'},
  {path:'**',redirectTo:'/user-home',pathMatch:'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
