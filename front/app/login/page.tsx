"use client";

import { useRouter } from "next/navigation";
import { useDispatch } from "react-redux";
import { login } from "../redux/slices/authSlice";
import { loginBack } from "../services/authService";

export default function Login() {
    const router = useRouter();
    const dispatch = useDispatch();

    const handlerLogin = async (formData: FormData) => {
        try {
            const email = formData.get("email")?.toString() ?? "";
            const senha = formData.get("senha")?.toString() ?? "";

            const resposta = await loginBack(email, senha);
            dispatch(login(resposta));
            router.push("/home");
        } catch (error) {
            alert("E-mail ou senha inválidos!");
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-purple-50 px-4">
            <div className="w-full max-w-md bg-white rounded-2xl shadow-lg p-8">
                <h1 className="text-3xl font-bold text-purple-700 mb-6">Entrar no sistema</h1>
                <form action={handlerLogin} className="space-y-6">
                    <div>
                        <label className="block text-purple-900 mb-2">E-mail</label>
                        <input name="email" type="email" required
                            className="w-full rounded-lg border border-purple-200 px-4 py-3 text-gray-700"
                            placeholder="Digite seu e-mail" />
                    </div>
                    <div>
                        <label className="block text-purple-900 mb-2">Senha</label>
                        <input name="senha" type="password" required
                            className="w-full rounded-lg border border-purple-200 px-4 py-3 text-gray-700"
                            placeholder="Digite sua senha" />
                    </div>
                    <button type="submit"
                        className="w-full rounded-lg bg-purple-600 py-3 text-white font-semibold hover:bg-purple-700">
                        Entrar
                    </button>
                </form>
            </div>
        </div>
    );
}
