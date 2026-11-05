import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import type { Usuario } from "@/app/types/usuario";

interface AuthState {
    token: string;
    usuario: Usuario | null;
}

const initialState: AuthState = {
    token: "",
    usuario: null
};

const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        login: (state, action: PayloadAction<{ token: string; usuario: Usuario }>) => {
            state.token = action.payload.token;
            state.usuario = action.payload.usuario;
        },
        logout: (state) => {
            state.token = "";
            state.usuario = null;
        }
    }
});

export const { login, logout } = authSlice.actions;
export default authSlice.reducer;
