import { Passenger } from "./passenger.model";
import { TicketStatus } from "./ticket-status.model";

export interface Ticket {
  ticketNo: number;
  pnrNo: string;
  trainId: number;
  trainName: string;
  date: string;
  source: string;
  destination: string;
  arrivalTime: string;
  departureTime: string;
  classType: string;
  status: TicketStatus;
  totalAmount: number;
  coachId: number;
  passengers: Passenger[];
  chargeId: string;
  username: string;
  token: string;
}
