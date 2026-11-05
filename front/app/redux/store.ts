import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./slices/authSlice";
import pacienteReducer from "./slices/pacienteSlice";

export const store = configureStore({
    reducer: {
        auth: authReducer,
        pacientes: pacienteReducer
    }
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
