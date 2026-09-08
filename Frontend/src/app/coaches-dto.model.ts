import { SeatDTO } from "./seat-dto.model";

export interface CoachesDTO {
    coachNumber: string;
    classType: string;
    totalSeats: number;
    availableSeats: number;
    // racCapacity: number;
    // waitingCapacity: number;
    // currentRacCount: number;
    // currentWaitingCount: number;
    fare: number;
    seats: SeatDTO[];
    availabilityChecked?: boolean; // optional flag
}