import { BookingStatus } from "./booking-status.model";

export interface Passenger{
    
//   passengerId: number;
  passengerName: string;
  age: number;
  gender: string;
  email: string;
  seatNumber:string;
  coachNumber:string;
  trainName: string,
  pnrNo: string,
  source: string,
  destination: string
  trainId: number;
  status: BookingStatus
}