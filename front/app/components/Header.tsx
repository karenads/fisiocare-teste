"use client";

import { useSelector, useDispatch } from "react-redux";
import { useRouter } from "next/navigation";
import type { RootState } from "@/app/redux/store";
import { logout } from "@/app/redux/slices/authSlice";
import { limparPacientes } from "@/app/redux/slices/pacienteSlice";
import { sairBack } from "@/app/services/authService";

export default function Header() {
    const usuario = useSelector((state: RootState) => state.auth.usuario);
    const dispatch = useDispatch();
    const router = useRouter();

    const sair = async () => {
        try {
            await sairBack();
        } catch (error) {
            console.error(error);
        }
        dispatch(logout());
        dispatch(limparPacientes());
        router.replace("/login");
    };

    return (
        <header className="flex items-center justify-between bg-white border-b border-purple-200 px-6 py-4">
            <span className="font-semibold text-purple-900">
                {usuario?.nome} {usuario?.role ? " - " + usuario.role.replace("ROLE_", "") : ""}
            </span>
            <button onClick={sair} className="rounded-lg bg-purple-700 px-4 py-2 text-white">
                Sair
            </button>
        </header>
    );
}
