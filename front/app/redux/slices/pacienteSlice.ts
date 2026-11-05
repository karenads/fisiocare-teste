import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import type { Paciente } from "@/app/types/paciente";

const initialState: Paciente[] = [];

const pacienteSlice = createSlice({
    name: "pacientes",
    initialState,
    reducers: {
        definirPacientes: (_, action: PayloadAction<Paciente[]>) => action.payload,
        limparPacientes: () => []
    }
});

export const { definirPacientes, limparPacientes } = pacienteSlice.actions;
export default pacienteSlice.reducer;
