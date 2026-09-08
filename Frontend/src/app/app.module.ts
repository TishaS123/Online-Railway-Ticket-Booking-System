import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AddTrainComponent } from './add-train/add-train.component';
import { AddFareComponent } from './add-fare/add-fare.component';
import { AddSeatComponent } from './add-seat/add-seat.component';
import { AddCoachComponent } from './add-coach/add-coach.component';
import { AdminHomeComponent } from './admin-home/admin-home.component';
import { UserHomeComponent } from './user-home/user-home.component';
import { ReservationsComponent } from './reservations-details/reservations.component';
import { TrainlistComponent } from './trainlist/trainlist.component';
import { PassengerFormComponent } from './passenger-form/passenger-form.component';
import { DatePipe } from '@angular/common';
import { PaymentComponent } from './payment/payment.component';
import { ShowTicketComponent } from './show-ticket/show-ticket.component';
import { PassengerListComponent } from './passenger-list/passenger-list.component';
import { ManageTrainComponent } from './manage-train/manage-train.component';
import { ManageFareComponent } from './manage-fare/manage-fare.component';
import { ManageCoachComponent } from './manage-coach/manage-coach.component';
import { ManageSeatComponent } from './manage-seat/manage-seat.component';
import { EditFareComponent } from './edit-fare/edit-fare.component';
import { EditTrainComponent } from './edit-train/edit-train.component';
import { EditCoachComponent } from './edit-coach/edit-coach.component';
import { EditSeatComponent } from './edit-seat/edit-seat.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { MyReservationComponent } from './my-reservation/my-reservation.component';
import { MakePaymentComponent } from './make-payment/make-payment.component';
import { JwtInterceptor } from './interceptors/jwt.interceptor';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    AddTrainComponent,
    AddFareComponent,
    AddSeatComponent,
    AddCoachComponent,
    AdminHomeComponent,
    UserHomeComponent,
    ReservationsComponent,
    TrainlistComponent,
    PassengerFormComponent,
    PaymentComponent,
    ShowTicketComponent,
    PassengerListComponent,
    ManageTrainComponent,
    ManageFareComponent,
    ManageCoachComponent,
    ManageSeatComponent,
    EditFareComponent,
    EditTrainComponent,
    EditCoachComponent,
    EditSeatComponent,
    MyReservationComponent,
    MakePaymentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    
    BrowserAnimationsModule, // required for toastr animations
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
    }),

  ],
  providers: [
    DatePipe,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
