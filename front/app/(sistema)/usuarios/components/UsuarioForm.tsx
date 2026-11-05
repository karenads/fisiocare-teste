"use client";

import { Usuario, UsuarioFormProps } from "@/app/types/usuario";
import { salvarUsuario, atualizarUsuario } from "@/app/services/usuarioService";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { useState } from "react";

export default function UsuarioForm({ usuarioExistente }: UsuarioFormProps) {
    const router = useRouter();
    const [usuario, setUsuario] = useState<Usuario>(
        usuarioExistente || new Usuario(null, "", "", "ATIVO", "", "")
    );

    const alterar = (campo: "nome" | "email" | "cpf" | "senha", valor: string) => {
        setUsuario({ ...usuario, [campo]: valor });
    };

    const salvar = async (formData: FormData) => {
        try {
            if (usuarioExistente) {
                await atualizarUsuario(usuario);
            } else {
                await salvarUsuario(usuario);
            }
            alert("Usuário salvo com sucesso!");
            router.push("/usuarios");
        } catch (error) {
            alert("Erro ao salvar usuário! Verifique os dados.");
        }
    };

    return (
        <form action={salvar} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {([
                    ["nome", "Nome completo", "text"],
                    ["cpf", "CPF", "text"],
                    ["email", "E-mail", "email"],
                    ["senha", "Senha", "password"]
                ] as const).map(([campo, titulo, tipo]) => (
                    <div key={campo}>
                        <label className="block mb-2 text-purple-800">{titulo}</label>
                        <input type={tipo} name={campo}
                            required={campo !== "senha" || !usuarioExistente}
                            value={usuario[campo]}
                            onChange={e => alterar(campo, e.target.value)}
                            className="w-full rounded-lg border border-purple-200 p-3 text-gray-800" />
                    </div>
                ))}
            </div>
            <div className="flex justify-end gap-4">
                <Link href="/usuarios" className="rounded-lg bg-purple-100 px-5 py-3 text-purple-800">Cancelar</Link>
                <button type="submit" className="rounded-lg bg-purple-700 px-5 py-3 text-white">Salvar</button>
            </div>
        </form>
    );
}
